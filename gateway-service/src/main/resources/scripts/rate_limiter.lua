redis.replicate_commands()

-- keys
local tokens_key = KEYS[1]
local timestamp_key = KEYS[2]
local blocked_key = KEYS[3]

-- args
local ttl = tonumber(ARGV[1])
local capacity = tonumber(ARGV[2])
local now = tonumber(ARGV[3])
local requested = tonumber(ARGV[4])
local blockedTtl = tonumber(ARGV[5])
local block_limit = tonumber(ARGV[6]) -- block limit value

if now == nil then
  now = redis.call('TIME')[1]
end

local blocked_count = tonumber(redis.call("get", blocked_key))
if blocked_count ~= nil and blocked_count >= block_limit then
  return { -1, 0 }
end

local last_tokens = tonumber(redis.call("get", tokens_key))
if last_tokens == nil then
  last_tokens = capacity
end

local last_refreshed = tonumber(redis.call("get", timestamp_key))
if last_refreshed == nil then
  last_refreshed = 0
end

local delta = math.max(0, now - last_refreshed)
local filled_tokens = math.min(capacity, last_tokens + delta)
local allowed = filled_tokens >= requested
local new_tokens = filled_tokens
local allowed_num = 0

if allowed then
  new_tokens = filled_tokens - requested
  allowed_num = 1
else
  redis.call("incr", blocked_key)
  redis.call("expire", blocked_key, blockedTtl)
end

if ttl > 0 then
  redis.call("setex", tokens_key, ttl, new_tokens)
  redis.call("setex", timestamp_key, ttl, now)
end

return { allowed_num, new_tokens }

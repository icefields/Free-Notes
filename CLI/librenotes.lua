local http = require("socket.http")
local json = require("dkjson")

-- Function to format date
local function formatDate(timestamp)
    return os.date("%Y-%m-%d %H:%M:%S", os.time{
        year=tonumber(timestamp:sub(1,4)), 
        month=tonumber(timestamp:sub(6,7)), 
        day=tonumber(timestamp:sub(9,10)), 
        hour=tonumber(timestamp:sub(12,13)), 
        min=tonumber(timestamp:sub(15,16)), 
        sec=tonumber(timestamp:sub(18,19))
    })
end

-- Check if --dates or -d option is provided
local displayDates = false
for i = 1, #arg do
    if arg[i] == "--dates" or arg[i] == "-d" then
        displayDates = true
        break
    end
end

-- Parameters to be passed to index.php
local param_value = { }
for i = 1, #arg do
    if arg[i]:sub(1, 1) ~= "-" then
        table.insert(param_value, arg[i])
    end
end

-- Construct the URL with the provided parameter value
local url = "http://192.168.1.100/freenotes/api.php?action=view"
if param_value then
    url = url .. "&username=" .. param_value[1]
end

-- Fetch JSON data from the URL
local response, status = http.request(url)

-- Check if the request was successful
if status ~= 200 then
    print("Error fetching JSON data")
    return
end

-- Decode JSON data
local data = json.decode(response)

-- Check if JSON data was successfully retrieved
if not data then
    print("Error decoding JSON data")
    return
end

-- Display JSON data with formatted dates
for _, item in ipairs(data.data) do
    if (param_value[2] ~= nil) then 
        if (param_value[2] == item.title) then
            print(item.message)
        end
    else
        print("Title: " .. item.title)
        print("Message: " .. item.message)
        if displayDates then
            print("Date Created: " .. formatDate(item.date_created))
            print("Date Modified: " .. formatDate(item.date_modified))
        end
        print()  -- Empty line to separate elements
    end
end


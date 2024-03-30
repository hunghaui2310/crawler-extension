import asyncio
import websockets

# Store a reference to connected clients
connected_clients = set()

async def echo(websocket, path):
    # Add the client to the set of connected clients
    connected_clients.add(websocket)
    
    try:
        async for message in websocket:
            # Echo received messages back to the client
            await websocket.send(message)
    finally:
        # Remove the client from the set of connected clients when the connection is closed
        connected_clients.remove(websocket)

async def send_message():
    while True:
        # Send a message to each connected client
        for client in connected_clients:
            await client.send("Hello from server!")
        # Wait for 5 seconds before sending the next message
        await asyncio.sleep(5)

start_server = websockets.serve(echo, "localhost", 8765)

asyncio.get_event_loop().run_until_complete(start_server)

# Start a task to send messages to clients asynchronously
asyncio.ensure_future(send_message())

# Start the event loop
asyncio.get_event_loop().run_forever()

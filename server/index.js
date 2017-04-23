var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var phone = [];

server.listen(3000, function(){
	console.log("Server is now running...");
});

io.on('connection', function(socket){
	console.log("Player Connected!");
	socket.emit('socketID', { id: socket.id });
	socket.emit('getPlayers', players);
	socket.broadcast.emit('newPlayer', { id: socket.id });

	socket.on('playerMoved', function (data) {
		data.id = socket.id;
		socket.broadcast.emit('playerMoved', data);

		for(var i = 0; i<players.length; i++)
		{
			if(players[i].id==data.id)
			{
				players[i].x = data.x;
				players[i].y = data.y;
			}
		}
    });

	socket.on('phoneDroopped', function (data) {
		data.id = socket.id;
		socket.broadcast.emit('phodeDropped', data);
		if (phone.empty()) {
			phone.push(data.x, data.y);
			players.push(3, phone.x, phone.y);
		}



		// players.push
		// for(var i = 0; i<players.length; i++)
		// {
		// 	if(players[i].id==data.id)
		// 	{
		// 		players[i].x = data.x;
		// 		players[i].y = data.y;
		// 	}
		// }
	});

	socket.on('disconnect', function(){
		console.log("Player Disconnected");
		socket.broadcast.emit('playerDisconnected', { id: socket.id });
		for(var i = 0; i < players.length; i++){
			if(players[i].id == socket.id){
				players.splice(i, 1);
			}
		}
	});
	players.push(new player(socket.id, 0, 0));
});

function player(id, x, y, vloger){
	this.id = id;
    // this.vloger = vloger;
    // this.camera = camera
	this.x = x;
	this.y = y;
}

function phone(x, y) {
	this.xPhone = x;
	this.yPhone = y;
}


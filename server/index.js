var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var phones = [];

server.listen(5665,'0.0.0.0');

io.on('connection', function(socket){
	console.log("Player Connected!");
	socket.emit('socketID', { id: socket.id });
	socket.emit('getPlayers', players);
	//socket.emit('getPhone', phones);
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

	socket.on('phoneDropped', function (data) {
		data.id = socket.id;
		console.log("Telephone ypal");
		//socket.broadcast.emit('phoneDropped', data);
		phones.push(new phone(socket.id,data.x, data.y));
        for(var i = 0; i<phones.length; i++)
		{
			if(phones[i].id==phones.id)
			{
				phones[i].x = data.x;
				phones[i].y = data.y;
			}
		}

	  //  socket.emit('getPhone', phones);





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

function phone(id, x, y) {
    this.id = id;
	this.xPhone = x;
	this.yPhone = y;
}


var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];
var phones = [];
var tmp = 0;

server.listen(5665,'0.0.0.0');

io.on('connection', function(socket){
	console.log("Player Connected!");
	socket.emit('socketID', { id: socket.id });
	socket.emit('getPlayers', players);
	tmp = getRandomArbitrary(1, 5);

	socket.broadcast.emit('newPlayer', { id: socket.id });
	socket.emit('getNum', {id: tmp});

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
		socket.emit('getPhone', phones);
    });

	socket.on('phoneDropped', function (data) {
	    if(phones.length<2)
	    {
            data.id = socket.id;
            console.log("Telephone ypal");
            //socket.broadcast.emit('phoneDropped', data);
            phones.push(new phone(socket.id,data.x, data.y));
            for(var i = 0; i<phones.length; i++)
            {
                if(phones[i].id==data.id)
                {
                    phones[i].x = data.x;
                    phones[i].y = data.y;
                }
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
function getRandomArbitrary(min, max) {
    return Math.random() * (max - min) + min;
}

/**
 * Returns a random integer between min (inclusive) and max (inclusive)
 * Using Math.round() will give you a non-uniform distribution!
 */
function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
function phone(id, x, y) {
    this.id = id;
	this.xPhone = x;
	this.yPhone = y;
}


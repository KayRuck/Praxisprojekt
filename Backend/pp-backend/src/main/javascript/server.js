var app = require('express')()
var server = require('http').createServer(app)
var io = require('socket.io')(server)

// Entwerder den Port 5454 nutzen oder was als enviorment var 'PORT' def ist
var port = process.env.PORT || 5454;

app.get('/h', (req, res) => {
    res.send('Hallo Kay')
})

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/index.html')
})

io.on('connection', (socket) => {
    console.log('a user connected')
    socket.on('disconnect', () => {
        console.log('user disconnected')
    })
})

server.listen(port, () => {
    console.log('Webserver läuft und hört auf Port %d', port)
})

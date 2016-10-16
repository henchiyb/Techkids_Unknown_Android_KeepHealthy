var express = require('express');
var app = express();
var fs = require("fs");


app.get('/breathExercise', function (req, res) {
   fs.readFile( __dirname + "/" + "exercise_breath.json", 'utf8', function (err, data) {
      console.log( data );
      res.end( data );
   });
})

app.get('/officeExercise', function (req, res) {
   fs.readFile( __dirname + "/" + "exercise_office.json", 'utf8', function (err, data) {
      console.log( data );
      res.end( data );
   });
})

app.get('/physicalExcercise', function (req, res) {
   fs.readFile( __dirname + "/" + "exercise_physical.json", 'utf8', function (err, data) {
      console.log( data );
      res.end( data );
   });
})

 
var server = app.listen(8081, function () {
    var host = '10.64.1.105';
    var port = server.address().port;
    console.log('Server start at http://%s:%s', host, port);
});
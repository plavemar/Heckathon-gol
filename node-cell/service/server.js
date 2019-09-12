const express = require('express');
const bodyParser = require('body-parser');
const service = require('./service.js');
const collect = require('./pubsub.js');


const app = express();

app.use(bodyParser.json());

app.get('/', (req, res) => {
    service.resolve(req.body);
});

module.exports = app;
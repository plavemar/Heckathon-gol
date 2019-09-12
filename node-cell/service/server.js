const express = require('express');
const bodyParser = require('body-parser');
const service = require('./service.js');

const app = express();

app.use(bodyParser.json());

app.get('/', (req, res) => {
    const response = service.resolve(req.body);
    res.status(200).send(response);
});

module.exports = app;
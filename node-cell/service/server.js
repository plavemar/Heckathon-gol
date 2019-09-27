const express = require('express');
const bodyParser = require('body-parser');
const service = require('./service.js');
const collect = require('./pubsub.js');

const app = express();

app.use(bodyParser.json());


const { PubSub } = require('@google-cloud/pubsub');
const pubsub = new PubSub();

const subscriptionName = 'CREATE_SUB';
const timeout = 60;

// References an existing subscription
const subscription = pubsub.subscription(subscriptionName);

// Create an event handler to handle messages
let messageCount = 0;
const messageHandler = message => {
    console.log(`Received message ${message.id}:`);
    console.log(`\tData: ${message.data}`);
    console.log(`\tAttributes: ${message.attributes}`);
    messageCount += 1;

    collect.collect(JSON.stringify(message.data));
    // "Ack" (acknowledge receipt of) the message
    message.ack();
};

// Listen for new messages until timeout is hit
subscription.on(`message`, messageHandler);

setTimeout(() => {
    subscription.removeListener('message', messageHandler);
    console.log(`${messageCount} message(s) received.`);
}, timeout * 1000);

module.exports = app;
const { PubSub } = require('@google-cloud/pubsub');

exports.collect = async function (message) {
    const pubsub = new PubSub();

    const dataBuffer = Buffer.from(message);

    const messageId = await pubsub.topic('COLLECT').publish(dataBuffer);
    console.log("Message pushed with id: ", messageId);
}
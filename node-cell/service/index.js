const service = require('./service.js');

exports.pubSubMethod = (pubSubEvent, context) => {
    const pubSubData = Buffer.from(pubSubEvent.data, 'base64').toString();
    console.log(`Hello, ${pubSubData}`);
    service.resolve(pubSubData);
  };
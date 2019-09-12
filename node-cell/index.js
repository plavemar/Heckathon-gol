const service = require('./service/service.js');

exports.pubSubMethod = (pubSubEvent, context) => {
    const pubSubData = Buffer.from(pubSubEvent.data, 'base64').toString();
    console.log(`Hello, ${pubSubData}`);
    service.resolve(JSON.parse(`${pubSubData}`));
  };
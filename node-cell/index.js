const service = require('./service/service.js');

exports.pubSubMethod = (pubSubEvent, context) => {
    const pubSubData = Buffer.from(pubSubEvent.data, 'base64').toString();
    service.resolve(JSON.parse(`${pubSubData}`));
  };
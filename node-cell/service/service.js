const collect = require('./pubsub');

const getNeighbourCount = (neighbours) => {
    let count = 0;
    neighbours.map((cell) => {
        count += cell;
    })
    console.log("Neighbour count: ", count);
    return count;
}

exports.pubSubMethod = (pubSubEvent, context) => {
    const pubSubData = Buffer.from(pubSubEvent.data, 'base64').toString();
    console.log(`Hello, ${pubSubData}`);
    response = resolve(pubSubData)
    console.log(response);
  };

function resolve(request) {
    let response = {
        generation: request.generation + 1,
        position: request.position,
    };

    const neighbourCount = getNeighbourCount(request.neighbours);
    let status;
    if(request.state === 1) {
        // Cell is alive, rule No. 1 - 3
        if(neighbourCount === 2 || neighbourCount === 3) {
            status = 1;
        } else {
            status = 0;
        }
    } else if(request.state === 0) {
        // Cell is dead, rule No. 4
        if(neighbourCount === 3) {
            status = 1;
        } else {
            status = 0;
        }
    } else {
        // invalid state - not implemented...
    }

    const message = Object.assign({}, response, {
        state: status
    });
    
    collect.collect(JSON.stringify(message));
}
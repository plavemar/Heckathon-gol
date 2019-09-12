const getNeighbourCount = (neighbours) => {
    let count = 0;
    neighbours.map((cell) => {
        count += cell;
    })
    console.log("Neighbour count: ", count);
    return count;
}

exports.resolve = function resolve(request) {
    let response = {
        generation: request.generation,
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

    return Object.assign({}, response, {
        state: status
    });
}
const getNeighbourCount = (neighbours) => {
    let count = 0;
    neighbours.map((cell) => {
        count += cell;
    })

    return count;
}

function resolve(request) {
    let response = {
        generation: request.generation,
        position: request.position,
    };

    const neighbourCount = this.getNeighbourCount
    if(request.status === 1) {
        // Cell is alive, rule No. 1 - 3
        if(neighbourCount === 2 | 3) {
            response.status = 1;
        } else {
            response.status = 0;
        }
    } else if(request.status === 0) {
        // Cell is dead, rule No. 4
        if(neighbourCount === 3) {
            response.status = 1;
        } else {
            response.status = 0;
        }
    } else {
        // invalid state - not implemented...
    }
    return response;
}
var data = {
    state: 0,
	generation: 1,
	position: {
		x: 1,
		y: 1
	},
	neighbours: [0,0,1]
};

var expectData = {
	generation: 2,
	position: {
		x: 1,
		y: 1
    },
    state: 0
};

const serviceTest = require('../service/service.js');

test('Dead cell -> dead call', () => {
    expect(expectData).toStrictEqual(serviceTest.resolve(data));
});


var data2 = {
    state: 1,
	generation: 1,
	position: {
		x: 1,
		y: 1
	},
	neighbours: [0,0,0,0,1,0,1,1]
};

var expectData2 = {
	generation: 2,
	position: {
		x: 1,
		y: 1
    },
    state: 1
};

test('Live cell -> live call', () => {
    expect(expectData2).toStrictEqual(serviceTest.resolve(data2));
});


var data3 = {
    state: 1,
	generation: 1,
	position: {
		x: 1,
		y: 1
	},
	neighbours: [0,0,0,0,0,0,0,1]
};

var expectData3 = {
	generation: 2,
	position: {
		x: 1,
		y: 1
    },
    state: 0
};

test('Live cell -> dead call', () => {
    expect(expectData3).toStrictEqual(serviceTest.resolve(data3));
});


var data4 = {
    state: 0,
	generation: 1,
	position: {
		x: 1,
		y: 1
	},
	neighbours: [1,0,0,1,0,0,0,1]
};

var expectData4 = {
	generation: 2,
	position: {
		x: 1,
		y: 1
    },
    state: 1
};

test('Dead cell -> live call', () => {
    expect(expectData4).toStrictEqual(serviceTest.resolve(data4));
});
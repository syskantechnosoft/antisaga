const axios = require('axios');

const GATEWAY_URL = 'http://localhost:8080';
const USER = { username: 'e2etest', password: 'password', email: 'e2e@test.com', mobile: '1234567890' };
let userId = null;

async function runE2E() {
    console.log('--- Starting E2E Test ---');

    try {
        // 1. Register
        console.log('1. Registering User...');
        try {
            const regRes = await axios.post(`${GATEWAY_URL}/auth/register`, USER);
            userId = regRes.data.id;
            console.log('   User Registered with ID:', userId);
        } catch (e) {
            console.log('   User might already exist, trying login...');
        }

        // 2. Login (Verify credentials)
        console.log('2. Logging in...');
        const loginRes = await axios.post(`${GATEWAY_URL}/auth/login`, { username: USER.username, password: USER.password });
        if (loginRes.data && loginRes.data.username === USER.username) {
            console.log('   Login Successful');
            userId = loginRes.data.id; // Ensure we have ID
        } else {
            throw new Error('Login failed');
        }

        // 3. Create Order
        console.log('3. Creating Order...');
        const orderPayload = {
            userId: userId,
            items: [{ productId: 1, quantity: 1, price: 100 }], // Assume Product 1 exists
            totalAmount: 100
        };
        const orderRes = await axios.post(`${GATEWAY_URL}/order/create`, orderPayload);
        const orderId = orderRes.data.id;
        console.log('   Order Created:', orderId);

        // 4. Poll for SAGA Completion
        console.log('4. Polling Order Status (SAGA)...');
        let attempts = 0;
        let finalStatus = 'PENDING';

        while (attempts < 10) {
            await new Promise(r => setTimeout(r, 2000)); // Wait 2s
            const statusRes = await axios.get(`${GATEWAY_URL}/order/all`);
            const order = statusRes.data.find(o => o.id === orderId);

            if (order) {
                console.log(`   Attempt ${attempts + 1}: Status is ${order.orderStatus}`);
                if (order.orderStatus === 'ORDER_COMPLETED' || order.orderStatus === 'ORDER_CANCELLED') {
                    finalStatus = order.orderStatus;
                    break;
                }
            }
            attempts++;
        }

        if (finalStatus === 'ORDER_COMPLETED') {
            console.log('✅ E2E Test PASSED: Order Completed Successfully');
        } else {
            console.error('❌ E2E Test FAILED: Order Status is ' + finalStatus);
        }

    } catch (error) {
        console.error('❌ E2E Test Failed with Exception:', error.message);
        if (error.response) console.error('   Response:', error.response.data);
    }
}

runE2E();

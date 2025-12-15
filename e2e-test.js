const axios = require('axios');

const GATEWAY_URL = 'http://localhost:8080';
const USER = { username: 'e2etest', password: 'password', email: 'e2e@test.com', mobile: '9876543210' };
let userId = null;
const PRODUCT_ID = 1;

async function runE2E() {
    console.log('--- Starting E2E Test ---');

    try {
        // 1. Register or Login
        console.log('1. Authenticating User...');
        try {
            const regRes = await axios.post(`${GATEWAY_URL}/auth/register`, USER);
            userId = regRes.data.id;
            console.log('   User Registered with ID:', userId);
        } catch (e) {
            console.log('   User might already exist, trying login...');
        }

        const loginRes = await axios.post(`${GATEWAY_URL}/auth/login`, { email: USER.email, password: USER.password });
        if (!userId) userId = loginRes.data.id;
        console.log('   Logged in as ID:', userId);

        // 2. Get Initial States
        console.log('2. Checking Initial State...');
        let initialBalance = 0;
        let initialInventory = 0;

        try {
            const balanceRes = await axios.get(`${GATEWAY_URL}/payment/users/${userId}/balance`);
            initialBalance = balanceRes.data.price;
            console.log('   Initial Balance:', initialBalance);
        } catch (e) { console.log('   Could not fetch balance (maybe new user)'); }

        try {
            const invRes = await axios.get(`${GATEWAY_URL}/inventory/${PRODUCT_ID}`);
            initialInventory = invRes.data.availableAmount;
            console.log(`   Initial Inventory for Product ${PRODUCT_ID}:`, initialInventory);
        } catch (e) { console.log('   Could not fetch inventory'); }


        // 3. Create Order
        console.log('3. Creating Order...');
        const orderPayload = {
            userId: userId,
            items: [{ productId: PRODUCT_ID, quantity: 1, price: 100 }],
            totalAmount: 100
        };
        const orderRes = await axios.post(`${GATEWAY_URL}/order/create`, orderPayload);
        const orderId = orderRes.data.id;
        console.log('   Order Created:', orderId);

        // 4. Poll for SAGA Completion
        console.log('4. Waiting for SAGA Completion...');
        let attempts = 0;
        let finalStatus = 'PENDING';

        while (attempts < 20) {
            await new Promise(r => setTimeout(r, 1000));
            const statusRes = await axios.get(`${GATEWAY_URL}/order/all`);
            const order = statusRes.data.find(o => o.id === orderId);

            if (order && (order.orderStatus === 'ORDER_COMPLETED' || order.orderStatus === 'ORDER_CANCELLED')) {
                finalStatus = order.orderStatus;
                break;
            }
            attempts++;
        }

        if (finalStatus === 'ORDER_COMPLETED') {
            console.log('   ✅ Order Completed via SAGA');

            // 5. Verify Post-Order States
            console.log('5. Verifying Data Updates...');

            try {
                const balanceRes = await axios.get(`${GATEWAY_URL}/payment/users/${userId}/balance`);
                const newBalance = balanceRes.data.price;
                console.log(`   Balance: ${initialBalance} -> ${newBalance}`);
                if (newBalance === initialBalance - 100) console.log('   ✅ Payment Verified (Balance Deducted)');
                else console.error('   ❌ Payment Verification Failed');
            } catch (e) { console.error('   ❌ Failed to check Payment'); }

            try {
                const invRes = await axios.get(`${GATEWAY_URL}/inventory/${PRODUCT_ID}`);
                const newInventory = invRes.data.availableAmount;
                console.log(`   Inventory: ${initialInventory} -> ${newInventory}`);
                if (newInventory === initialInventory - 1) console.log('   ✅ Inventory Verified (Stock Deducted)');
                else console.error('   ❌ Inventory Verification Failed');
            } catch (e) { console.error('   ❌ Failed to check Inventory'); }

        } else {
            console.error('❌ E2E Test FAILED: Final Status is ' + finalStatus);
        }

    } catch (error) {
        console.error('❌ E2E Test Failed with Exception:', error.message);
        if (error.response) console.error('   Response:', error.response.data);
    }
}

runE2E();

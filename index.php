<?php
require 'vendor/autoload.php';

    $stripe = new \Stripe\StripeClient('Your Stripe client key');

// Use an existing Customer ID if this is a returning customer.
    $customer = $stripe->customers->create(
        [
            'name' => "Bob",
            'address' => [
                'line1' => 'Address',
                'postal_code' => '738933',
                'city' => 'Mbarara',
                'state' => 'Mbarara',
                'country' => 'UGX'
            ]
        ]
    );
    $ephemeralKey = $stripe->ephemeralKeys->create([
        'customer' => $customer->id,
    ], [
        'stripe_version' => '2022-08-01',
    ]);
    $paymentIntent = $stripe->paymentIntents->create([
        'amount' => 135,//1.35 usd
        'currency' => 'usd',
        'description' => 'Toll Gate Payment',
        'customer' => $customer->id,
        'automatic_payment_methods' => [
            'enabled' => 'true',
        ],
    ]);

    echo json_encode(
        [
            'paymentIntent' => $paymentIntent->client_secret,
            'ephemeralKey' => $ephemeralKey->secret,
            'customer' => $customer->id,
            'publishableKey' => 'Your stripe publishable key'
        ]
    );
    http_response_code(200);

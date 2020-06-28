# Foreign Currency Account
> Bank account with sub account in different currencies

## Table of contents
* [General info](#general-info)
* [Features](#features)
* [Short Guide](#short-guide)
* [Contact](#contact)

## General info
The biggest goal of creating this application is to improve skills in Java and Spring

## Features
* Chect if the pesel is correct
* Check if the pesel owner is of age
* Currency exchange based on the exchange rate with https://api.nbp.pl/

## Short Guide
* Register bank account
```json
{
    "pesel": "90080957615",
    "firstName": "Tom",
    "lastName": "Sccot",
    "accountBalances": [
        {
            "balance": 2000,
            "currency": "PLN"
        }
    ]
}
```
Response
```json
{
    "pesel": "90080957615",
    "firstName": "Tom",
    "lastName": "Sccot",
    "accountBalances": [
        {
            "balance": 2000,
            "currency": "PLN"
        },
        {
            "balance": 0,
            "currency": "USD"
        }
    ]
}
```
* Exchange Money
```json
{
    "amount": 500,
    "from": "PLN",
    "to": "USD"
}
```
Response
```json
{
    "pesel": "90080957615",
    "firstName": "Tom",
    "lastName": "Sccot",
    "accountBalances": [
        {
            "balance": 1500.00,
            "currency": "PLN"
        },
        {
            "balance": 125.95,
            "currency": "USD"
        }
    ]
}
```

## Contact
Created by [@Patryk Piecek](https://www.linkedin.com/in/patryk-piecek-55543b187/)

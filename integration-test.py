#!/usr/bin/env python3

import requests

from requests.auth import HTTPBasicAuth

basic = HTTPBasicAuth('user', 'password')

resp = requests.get('http://localhost:8080/aggregate/1')
resp.raise_for_status()
data = resp.json()

resp = requests.get('http://localhost:8080/aggregate/3')
resp.raise_for_status()
data = resp.json()

assert data["rating"] is None

resp = requests.get('http://localhost:8080/aggregate/1000')
assert resp.status_code == 404

resp = requests.post('http://localhost:8081/rating/1/5')
assert resp.status_code == 401, "Unauthorized requests prohibited"

resp = requests.post('http://localhost:8081/rating/1/5', auth=basic)
resp.raise_for_status()

print("All tests passed successfully")

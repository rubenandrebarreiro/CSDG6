import requests;
import numpy as np
import matplotlib.pyplot as plt

import time

def register(user,password):
	r =requests.post("https://localhost:8443/register", json= {"userName": user, "password": password, "amount": 100, "roles":["ROLE_AUCTION_MAKER"]},verify=False)
	print(r.status_code)

def login(user,password):
	r =requests.post("https://localhost:8443/login", json= {"username": user,"password": password},verify=False)
	print(r.status_code)
	return r.headers['Authorization']


def createMoney(user,token):
	headers = {'Authorization': token}
	r =requests.put("https://localhost:8443/money?who="+user,headers = headers,json={"amount": 100},verify=False)
	print(r.status_code)

def transferMoney(user,token,user2,amount):	
	print("user " +user)
	print("token " + token)
	print("user 2 " + user2)
	print("amount "+str(amount))
	headers = {'Authorization': token}
	print("https://localhost:8443/amount?from="+user+"&to="+user2)
	r = requests.put("https://localhost:8443/amount?from="+user+"&to="+user2,json={"amount":amount},headers=headers,verify=False);
	print(r.status_code)

# username = "filipe"
# password = "p"
# u2 = "f"
# register(username,password);
# register(u2,password)
# token = login(username,password);
# transferMoney(username,token,u2,19)

# mapa = [];

# uu0 = "user0"
# pp0 = "password"
# time0 = 0;
# uu1 = "user1"
# pp1 = "password"
# time1 = 0;
# uu2 = "user2"
# pp2 = "password"
# time2 = 0;
# uu3 = "user3"
# pp3 = "password"
# time3 = 0;


# for i in range(50):
# 	start_time = time.time()
# 	register(uu3+str(i),pp3+str(i))
# 	time3 += time.time() - start_time	
# 	start_time = time.time()
# 	mapa.append([uu3+str(i),pp3+str(i),login(uu3+str(i),pp3+str(i))])
# 	time3 += time.time() - start_time

# for j in range(len(mapa)):
# 	k = mapa[j]
# 	if i == 49:
# 		k2 = mapa[0]
# 	else:
# 		k2 = mapa[j+1]
# 	start_time = time.time()
# 	transferMoney(k[0],k[2],k2[0],10)
# 	time3 += time.time() - start_time

# for i in range(100):
# 	start_time = time.time()
# 	register(uu2+str(i),pp2+str(i))
# 	time2 += time.time() - start_time	
# 	start_time = time.time()
# 	mapa.append([uu2+str(i),pp2+str(i),login(uu2+str(i),pp2+str(i))])
# 	time2 += time.time() - start_time

# for j in range(100):
# 	k = mapa[j]
# 	if i == 99:
# 		k2 = mapa[0]
# 	else:
# 		k2 = mapa[j+1]
# 	start_time = time.time()
# 	transferMoney(k[0],k[2],k2[0],10)
# 	time2 += time.time() - start_time

# for i in range(150):
# 	start_time = time.time()
# 	register(uu2+str(i),pp2+str(i))
# 	time2 += time.time() - start_time	
# 	start_time = time.time()
# 	mapa.append([uu2+str(i),pp2+str(i),login(uu2+str(i),pp2+str(i))])
# 	time2+= time.time() - start_time

# for j in range(150):
# 	k = mapa[j]
# 	if i == 149:
# 		k2 = mapa[0]
# 	else:
# 		k2 = mapa[j+1]
# 	start_time = time.time()
# 	transferMoney(k[0],k[2],k2[0],10)
# 	time2 += time.time() - start_time


# for i in range(200):
# 	start_time = time.time()
# 	register(uu0+str(i),pp0+str(i))
# 	time0 += time.time() - start_time	
# 	start_time = time.time()
# 	mapa.append([uu0+str(i),pp0+str(i),login(uu0+str(i),pp0+str(i))])
# 	time0 += time.time() - start_time

# for j in range(200):
# 	k = mapa[j]
# 	if i == 199:
# 		k2 = mapa[0]
# 	else:
# 		k2 = mapa[j+1]
# 	start_time = time.time()
# 	transferMoney(k[0],k[2],k2[0],10)
# 	time0 += time.time() - start_time


print("Time")
print(time0)
print(time1)
print(time2)
print(time3)
plt.plot([50,100,150,200], [121.80137085914612,0,161.2394299507141,33.859222173690796], 'ro')
# ax.set_xlabel('latency')
# ax.set_ylabel('number of operations')
# plt.axis([0, 6, 0, 20])
plt.show()
plt.savefig('latency.png')
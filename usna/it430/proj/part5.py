# part5.py

from Cryptodome.Cipher import AES
from Cryptodome.Util.Padding import pad
from Cryptodome.Util.Padding import unpad
from Cryptodome import Random 
from Cryptodome.Random import random

# Randomly chosen plaintext: Yes or No
M = [b"Yes", b"No"]

b = random.getrandbits(1) # 1-bit random: 0 or 1

# Random key and iv
key = Random.get_random_bytes(16)
iv = Random.get_random_bytes(16)

aes = AES.new(key, AES.MODE_CBC, iv)

print("iv:", iv.hex())
print("ct:", aes.encrypt(pad(M[b],AES.block_size)).hex())
print()

while True:
  iv = Random.get_random_bytes(16)
  print("iv:", iv.hex())
  pt = input("pt (hex): ").strip()  # choose your plaintext (as hexstring)

  if pt == "open":
    break

  pt = bytes.fromhex(pt)
  aes = AES.new(key, AES.MODE_CBC, iv)
  print("ct:", aes.encrypt(pad(pt,AES.block_size)).hex())
  print()

print("The secret was: ", M[b])

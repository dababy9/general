# project.py
# name: Caleb Walker
from Cryptodome.Cipher import AES
from Cryptodome.Util.Padding import pad
from Cryptodome.Util.Padding import unpad


# read in_plain_file, encrypt the data, and store the ciphertext in out_cipher_file
def encrypt_ecb(in_plain_file, out_cipher_file, key):
    open(out_cipher_file, "wb").write(AES.new(key, AES.MODE_ECB).encrypt(pad(open(in_plain_file, "rb").read(), 16)))

def encrypt_cbc(in_plain_file, out_cipher_file, key, iv):
    open(out_cipher_file, "wb").write(AES.new(key, AES.MODE_CBC, iv).encrypt(pad(open(in_plain_file, "rb").read(), 16)))

def encrypt_ctr(in_plain_file, out_cipher_file, key, ctr):
    open(out_cipher_file, "wb").write(AES.new(key, AES.MODE_CTR, nonce=ctr[0:8], initial_value=ctr[8:]).encrypt(open(in_plain_file, "rb").read()))



# read in_cipher_file, decrypt the ciphertext, and store the plaintext in out_plain_file
def decrypt_ecb(in_cipher_file, out_plain_file, key):
    open(out_plain_file, "wb").write(unpad(AES.new(key, AES.MODE_ECB).decrypt(open(in_cipher_file, "rb").read()), 16))

def decrypt_cbc(in_cipher_file, out_plain_file, key, iv):
    open(out_plain_file, "wb").write(unpad(AES.new(key, AES.MODE_CBC, iv).decrypt(open(in_cipher_file, "rb").read()), 16))

def decrypt_ctr(in_cipher_file, out_plain_file, key, ctr):
    open(out_plain_file, "wb").write(AES.new(key, AES.MODE_CTR, nonce=ctr[0:8], initial_value=ctr[8:]).decrypt(open(in_cipher_file, "rb").read()))



# read normal_bmp_file and in_cipher_file, fix the header in the ciphertext and
# store the results in out_cipher_bmp_file
def fix_bmp_header(normal_bmp_file, in_cipher_file, out_cipher_bmp_file):
    open(out_cipher_bmp_file, "wb").write(open(normal_bmp_file, "rb").read(54) + open(in_cipher_file, "rb").read()[54:])
    


#pad and unpad functions (implementing PKCS#5)
def pad2(data):
    result = bytearray(data)
    space = 16 - (len(data) % 16)
    for i in range(space): result.append(space)
    return bytes(result)

def unpad2(data):
    padlen, error = data[-1], False
    if padlen > 16 or padlen < 1: error = True
    elif len(data) % 16 != 0: error = True
    else:
        for i in range(padlen):
            if data[-(i+1)] != padlen: error = True
    if error:
        print("padding error!")
        return None
    return data[0:-padlen]
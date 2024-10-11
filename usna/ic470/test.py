from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.common.exceptions import TimeoutException

from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys

import os

####
# Set up Driver
chromium_options = Options()
chromium_options.add_argument('--headless')
chromium_options.add_argument('--no-sandbox')
chromium_options.add_argument('--disable-dev-shm-usage')
chromium_options.add_argument("--ignore-certificate-errors")

# This sample code supports Appium Python client >=2.3.0
# pip install Appium-Python-Client
# Then you can paste this into a file and simply run with Python

from appium import webdriver
from appium.options.common.base import AppiumOptions
from appium.webdriver.common.appiumby import AppiumBy

# For W3C actions
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.actions import interaction
from selenium.webdriver.common.actions.action_builder import ActionBuilder
from selenium.webdriver.common.actions.pointer_input import PointerInput

options = AppiumOptions()
options.load_capabilities({
	"platformName": "Android",
	"appium:automationName": "UIAutomator2",
	"appium:appActivity": "br.com.brunocarvalhs.friendssecrets.presentation.MainActivity",
	"appium:appPackage": "br.com.brunocarvalhs.friendssecrets",
	"appium:deviceName": "RQCW905DH4D",
	"appium:ensureWebviewsHavePages": True,
	"appium:nativeWebScreenshot": True,
	"appium:newCommandTimeout": 3600,
	"appium:connectHardwareKeyboard": True
})

driver = webdriver.Remote("http://127.0.0.1:4723", options=options)

el1 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(4)")
el1.click()
el2 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(4)")
el2.click()
el2.click()
el2.click()
el3 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(1)")
el3.click()
el4 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(0)")
el4.send_keys("Grupo teste")
el5 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().text(\"Descrição do grupo\")")
el5.click()
el6 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(1)")
el6.send_keys("🎉 Amigo Secreto 2024 - Descrição Divertida 🎉  Bem-vindos ao nosso querido Amigo Secreto! Este é um momento especial onde a diversão e a amizade se encontram, e todos nós temos a chance de surpreender e ser surpreendidos. Preparem-se para um jogo recheado de risadas, emoções e, claro, muitas surpresas!  🌟 Como Funciona:  Sorteio: Cada um de nós sorteou o nome de um amigo, e agora temos a responsabilidade de escolher um presente que vai agradar ou divertir essa pessoa. Vamos manter os nomes em segredo até a hora da revelação!  Presentes: Lembre-se, o presente deve ter um limite de valor. Seja criativo! Pode ser algo engraçado, útil, ou até mesmo algo que faça referência a um momento especial que vocês compartilharam.  Dicas e Pistas: Durante o evento, cada um de nós terá a chance de dar dicas sobre o seu amigo secreto, tornando o jogo ainda mais emocionante. Prepara-se para adivinhar!  Revelação: O grande momento da revelação! Vamos descobrir quem é o amigo secreto de cada um e abrir os presentes juntos. Preparem suas câmeras para capturar as reações!  🎁 Dicas de Presentes:  Um item relacionado a um hobby do amigo. Um livro que você acha que ele(a) vai amar. Um item de decoração divertido para a casa. Uma experiência, como ingressos para um evento ou um voucher para um jantar. 💬 Mensagem Final: Lembre-se, o mais importante não é o presente em si, mas a alegria de compartilhar momentos juntos. Que possamos celebrar a amizade, a gratidão e a diversão neste evento! Que comece a festa do Amigo Secreto! 🥳")
el7 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(0)")
el7.click()
el9 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(0)")
el9.send_keys("Gosto 1")
el9.send_keys("Gosto um")
el9.send_keys("Membro um")
el10 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(1)")
el10.send_keys("Gosto um")
el11 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(0)")
el11.click()
el12 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(2)")
el12.click()
el13 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(0)")
el13.send_keys("Membro dois")
el14 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(1)")
el14.send_keys("Gosto dois")
el15 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(0)")
el15.click()
el16 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(2)")
el16.click()
el17 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(0)")
el17.send_keys("Membro tres")
el18 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(1)")
el18.send_keys("Gosto tres")
el19 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(0)")
el19.click()
el20 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(2)")
el20.click()
el21 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(0)")
el21.send_keys("Membro quatro")
el22 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.EditText\").instance(1)")
el22.send_keys("Gosto quatro")
el23 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(0)")
el23.click()
el24 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(3)")
el24.click()
el25 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(11)")
el25.click()
el26 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(4)")
el26.click()
el27 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(0)")
el27.click()
el27.click()
el28 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(0)")
el28.click()
el29 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(0)")
el29.click()
el30 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(1)")
el30.click()
el31 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(9)")
el31.click()
el32 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(9)")
el32.click()
el33 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(0)")
el33.click()
el34 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(3)")
el34.click()
el35 = driver.find_element(by=AppiumBy.CLASS_NAME, value="android.widget.EditText")
el35.send_keys("123456")
el36 = driver.find_element(by=AppiumBy.CLASS_NAME, value="android.widget.Button")
el36.click()
el37 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(5)")
el37.click()
el38 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.widget.Button\").instance(4)")
el38.click()
el39 = driver.find_element(by=AppiumBy.CLASS_NAME, value="android.widget.Button")
el39.click()
el40 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(4)")
el40.click()
el41 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(3)")
el41.click()
el41.click()
el42 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(6)")
el42.click()
el43 = driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value="Light")
el43.click()
el44 = driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value="Dark")
el44.click()
el45 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(11)")
el45.click()
el45.click()
el46 = driver.find_element(by=AppiumBy.CLASS_NAME, value="android.widget.Button")
el46.click()
el47 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(9)")
el47.click()
el48 = driver.find_element(by=AppiumBy.CLASS_NAME, value="android.widget.Button")
el48.click()
el49 = driver.find_element(by=AppiumBy.ANDROID_UIAUTOMATOR, value="new UiSelector().className(\"android.view.View\").instance(12)")
el49.click()
el50 = driver.find_element(by=AppiumBy.CLASS_NAME, value="android.widget.Button")
el50.click()
el51 = driver.find_element(by=AppiumBy.CLASS_NAME, value="android.widget.Button")
el51.click()
driver.execute_script('mobile: pressKey', {"keycode": 3})

driver.quit()
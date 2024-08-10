## Запуск программы:
1.	Скачать проект из GitHub;
2.	В терминале перейти в папку с проектом, выполнить билд (команда `mvn clean package`).
3.	Изучить [подробное описание работы приложения](./Application_description.pdf).
4.	В приложении уже создана учетная запись с привязанным к ней аккаунтом на платформе Bitmex. Для тестирования приложения необходимо [на сайте](https://testnet.bitmex.com) войти в учетную запись (**email: <span>bitmexproject@zohomail.com</span>**, **password: bitmexpassword**), либо создать новый аккаунт. В случае регистрации нового аккаунта нужно дополнительно получить apiKey и apiSecretKey для привязывания к учетной записи приложения [по ссылке](https://testnet.bitmex.com/app/apiKeys) (key permissions – Order);
5.	Для создания контейнеров в докере и запуска приложения через терминал выполнить последовательно команды:
```
docker-compose build
docker-compose up
```
6.	В браузере перейти по адресу [localhost:8080/api](https://localhost:8080/api). В случае использования предложенного аккаунта войти в гостевую учетную запись (**login: guest**, **password: guest**), либо последовательно пройти процедуры регистрации (вместе с логином и паролем ввести apiKey и apiSecretKey, которые были получены на этапе 4) и аутентификации.
7.	После успешной аутентификации по адресу [localhost:8080/api](https://localhost:8080/api) будет доступна страница c входными параметрами работы web-бота, которые необходимо заполнить:
- _Choose real or test platform_ – доступен только вариант TEST, при этом в приложении предусмотрена возможность быстрого добавления URL реальной биржи;
- _Demonstration mode_ – для быстрой оценки работы приложения необходимо выбрать YES. Принцип работы приложения в демонстрационном режиме представлено [в описании](./Application_description.pdf);
- _Choose product_ – доступен только вариант XBTUSD, предусмотрено быстрое добавление других продуктов;
- _Enter initial quality of one order_ - предлагаемое значение – 100;
- _Enter price step_ - предлагаемое значение - 100;
- _Enter number of Fibonacci levels (from 5 to 10)_ – можно выбрать любое из диапазона;
- _Choose number of algorithm cycles_ – доступные варианты представлены выпадающим списком;

	После заполнения нажать кнопку **Create**. Далее появится страница с предупреждением о том, что ордера, созданные вне работы данного цикла алгоритма, будут проигнорированы приложением. Нажать кнопку **Start!**.
8.	После появления страницы с надписью _Algorithm is working…_  перейти на вкладку с Bitmex (пункт 4). Дальнейшее взаимодействие с активными ордерами в демонстрационном режиме представлено [в описании](./Application_description.pdf).

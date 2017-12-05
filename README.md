# isilon


## 1.Create a new server.crt file

BeijingCSC-1# mkdir /ifs/local
BeijingCSC-1# cd /ifs/local


BeijingCSC-1# cp /usr/local/apache2/conf/ssl.key/server.key .
BeijingCSC-1# ls
server.key

Generate a new crt file;

BeijingCSC-1# openssl req -new -days 730 -nodes -x509 -key server.key -out server.crt
You are about to be asked to enter information that will be incorporated
into your certificate request.
What you are about to enter is what is called a Distinguished Name or a DN.
There are quite a few fields but you can leave some blank
For some fields there will be a default value,
If you enter '.', the field will be left blank.
-----
Country Name (2 letter code) [AU]:CN
State or Province Name (full name) [Some-State]:Beijing
Locality Name (eg, city) []:Beijing
Organization Name (eg, company) [Internet Widgits Pty Ltd]:EMC
Organizational Unit Name (eg, section) []:Isilon
Common Name (e.g. server FQDN or YOUR name) []:api.isilon.com
Email Address []:support@isilon.com
BeijingCSC-1# ls
server.crt	server.key

BeijingCSC-1# pwd
/ifs/local


## 2. Backup and Replace the default crt and key file.

Backup default key and crt first:
isi_for_array -s 'cp /usr/local/apache2/conf/ssl.key/server.key /ifs/local/server.key.bak'
isi_for_array -s 'cp /usr/local/apache2/conf/ssl.crt/server.crt /ifs/local/server.crt.bak' 


Replace key and crt:
isi services -a isi_webui disable 
chmod 640 server.key 
isi_for_array -s 'cp /ifs/local/server.key  /usr/local/apache2/conf/ssl.key/server.key' 
isi_for_array -s 'cp /ifs/local/server.crt /usr/local/apache2/conf/ssl.crt/server.crt' 
isi services -a isi_webui enable 

Now, you can access the Isilon Web GUI to confirm the certification has been updated.


## 3.Download server.crt file from Isilon to local laptop(xftp or sftp etc.).

Isilon: /usr/local/apache2/conf/ssl.crt/server.crt 
local laptop directory: C:\Users\gaoz2\Downloads\

## 4. Import server.crt into JRE cacerts.

"C:\Program Files\Java\jre1.8.0_121\bin\keytool.exe" -import -v -trustcacerts -alias api.isilon.com -file "C:\Users\gaoz2\Downloads\server.crt" -keystore "C:\Program Files\Java\jre1.8.0_151\lib\security\cacerts" -keypass changeit -storepass changeit

## 5.Check the crt has been imported.

"C:\Program Files\Java\jre1.8.0_121\bin\keytool.exe" -list -v -keystore "C:\Program Files\Java\jre1.8.0_151\lib\security\cacerts" | findstr api
输入密钥库口令:  changeit
别名: api.isilon.com
所有者: EMAILADDRESS=support@isilon.com, CN=api.isilon.com, OU=Isilon, O=EMC, L=Beijing, ST=Beijing, C=CN
发布者: EMAILADDRESS=support@isilon.com, CN=api.isilon.com, OU=Isilon, O=EMC, L=Beijing, ST=Beijing, C=CN


## 6. Add DNS resolv record to C:\Windows\System32\drivers\etc\hosts
10.32.32.132        api.isilon.com

ping api.isilon.com


## 7. Coding and Test.
	String HOST = "api.isilon.com";
	String USER = "root";
	String PASSWORD = "Password1!";
	String PORT = "8080";



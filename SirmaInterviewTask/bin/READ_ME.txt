Бележки:
Tools: Eclipse, Java8.
За UI съм използвал e(fx)clipse plugin на eclipse за JavaFX.
Програмата служи за намиране на двойка служители които са работили най-дълго време в дни по даден проект. 
Като намира всички интервали в които тази двойка са работили заедно по този проект(предолагаме че е възможно повече от веднъж да са работили заедно по един проект).
След избиране на файл от UI-а(единствено .txt файл) този файл се прочита(като се пропуска първият ред, той се брои за header) и всеки ред от него се преобразува в RawData обект и тези обекти се събират в една колекция.
След което тази колекция се преобразува в MAP с ключ Project_ID и стойност лист от RawData обекти който се състои от всички служители работили по този проект.
От този MAP намираме двойките служители които са работили заедно по един проект и всички интервали в които са работили заедно. Всяка една такава двойка е EmployeeCouple обект.
Създаваме МAP с ключ Project_ID и стойност лист от всички такива двойки и от него намираме тази двойка, които са работили най-дълго(в дни) по този проект.
Намирането на итервалите в които са работили по един проект става като намираме интервала между по-голямата започваща дата и по-малката крайна дата на периода в който служителите са работили по този проект.
Note: Не се проверява дали началната дата е след крайната. И не се проверява ако един служите има застъпващи се интервали по един проект.
Датите се четат в 3 формата изредени в медота createFormats() в класа DataRetriever;(само дати без час, минути етс.)




我開發的 ChatServer 是一個基於 Java 和 JavaSocket API 的簡單即時聊天伺服器，旨在讓使用者之間輕鬆交流。這個伺服器能在特定的埠上運行，並同時連接多個客戶端，支援即時訊息傳遞和用戶管理等功能。
當客戶端連接時，伺服器會為每個使用者分配一個獨立的執行緒，確保所有訊息能迅速廣播給在線的朋友。我設計了像 !userlist 和 !remove 這些指令，讓用戶可以查看當前在線的用戶或移除某個用戶，使互動更加靈活有趣。
在這個專案中，我選擇使用 Vector 來存儲使用者和客戶端資料，以確保在多執行緒環境中的安全性，同時透過 BufferedReader 和 PrintWriter 處理輸入和輸出，讓整個過程變得高效又簡單。這個聊天伺服器不僅功能全面，也讓我在實踐中學習了 Java 網路設計和多執行緒設計的基本概念。


The ChatServer I developed is a simple real-time chat server based on Java and the JavaSocket API, designed to facilitate easy communication between users. This server runs on a specific port and can connect multiple clients simultaneously, supporting instant message exchange and user management features.

When a client connects, the server assigns a separate thread for each user to ensure that all messages can be quickly broadcast to online friends. I designed commands like !userlist and !remove to allow users to view the currently online users or remove a specific user, making interactions more flexible and engaging.

In this project, I chose to use Vector to store user and client data to ensure safety in a multi-threaded environment, while BufferedReader and PrintWriter handle input and output, making the entire process efficient and straightforward. This chat server not only has comprehensive functionality but also allowed me to learn the fundamentals of Java network programming and multi-threaded design through hands-on practice.

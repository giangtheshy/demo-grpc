## Báo cáo về Grpc và protobuf:

### Lý thuyết:
- Grpc là giao thức thường dùng cho server call server, chủ yếu là dùng trong mô hình microservices
- Hiểu được cách hoạt động của grpc và ngôn ngữ protobuf

### Thực hành:
- Sử dụng grpc và protobuf với framework spring boot và maven
- Tạo cấu trúc file .proto, định nghĩa các message(giống với entity) ,service ở cả client và server
- Tạo các rpc (tương tự method) để truyền dữ liệu giữa server và client:
  + Truyền vào rpc method message của request và trả về message response. Có thể sử dụng stream request hoặc stream response để truyền 1 list các request,response.
  + ![alt text](https://res.cloudinary.com/giangtheshy/image/upload/v1629356679/all/dev/kvoyrricys15qw9ubwn3.jpg)
- Sau khi tạo xong file .proto thì sử dụng `mvn clean package -Dmaven.skip.test=true` khi dùng maven để generate source code được định nghĩa trong file .proto
- Setup server:
  + Tạo GrpcService và implement class BaseImpl của service đã định nghĩa
  + Override các method được định nghĩa trong file .proto để đón request từ client
  + Tạo server interceptor và add nó vào global grpc để quan sát mỗi khi server nhận request
- Setup client:
  + Khởi tạo blocking stub dùng cho single request và async stub để dùng cho stream request của client tới server
  + Tạo các endpoint để sử dụng rest api, sau khi gọi các api thì client sẽ truyền data được nhận đến server bằng grpc
  + Tạo client interceptor và add vào những service cần dùng

### Demo:
- Test grpc ở server:
  + Sử dụng BloomRpc để test api của server.
  + ![alt text](https://res.cloudinary.com/giangtheshy/image/upload/v1629355763/all/dev/ynzxtctgifxpwynl0zg2.gif)

- Test client:
  + Sử dụng postman để truyền data vào client.
  + ![alt text](https://res.cloudinary.com/giangtheshy/image/upload/v1629355775/all/dev/x56whfc5zk7anxktdiiq.gif)
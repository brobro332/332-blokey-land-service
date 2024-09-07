import axios from 'axios';

function App () {
  return (
    <div className="App">
        <button onClick={CreateMember}>
          테스트멤버등록
        </button>
    </div>
  );
}

function CreateMember () {
  axios.post("http://localhost:8080/api/v1/member", {
      email : "test@naver.com",
      password : "1234",
      name : "brobro332",
      description : "this is brobro332's react app!"
    }).then(function (response) {
      alert(response.data.message);
    }).catch(function (error) {
      alert(error);
    }).then(function () {
      // 항상 실행되는 부분
    });
}

export default App;

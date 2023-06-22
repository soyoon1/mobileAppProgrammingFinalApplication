package com.example.finalapplication

import com.google.gson.annotations.SerializedName


data class ItemRetrofitModel(
    var no: Long = 0,  // 순번명
    var fd_Nm: String? = null, // 음식명
    var fd_Wgh: String? = null,  // 식품 중량
    var ckry_Nm: String? = null, // 조리법 명
    var ckry_Sumry_Info: String? = null // 조리법 요약정보

)
data class MyList(val list: ItemRetrofitModel)
data class MyModel(val response: MutableList<MyList>)  // 이것을 서버로부터 받음.






// Json : JavaScript에서 데이터를 표현하는 방법, 웹에서 데이터를 주고받을 때 주로 사용
// Gson : json을 JAVA의 객체로 역직렬화, 직렬화 해주는 자바 라이브러리
// body.items[item.rnum]  items[item.rnum]을 recyclerView로 전달해주어야 함.

//var productGb: String? = null,  // 변수명이 반드시 같아야 함. 사진이 들어가면 좋겠음.
// 구조바꾸기
//    @SerializedName("data")

//data class ItemRetrofitModel(
//    var no: Long = 0,  // 순번명
//    var fd_Nm: String? = null, // 음식명
//    var fd_Wgh: String? = null,  // 식품 중량
//    var ckry_Nm: String? = null, // 조리법 명
//    var ckry_Sumry_Info: String? = null // 조리법 요약정보
//    //var productGb: String? = null,  // 변수명이 반드시 같아야 함. 사진이 들어가면 좋겠음.
//    // 구조바꾸기
//)
//data class MyItem(val item: ItemRetrofitModel)
//data class MyItems(val items: MutableList<MyItem>)
//data class MyModel(val body: MyItems)  // 이것을 서버로부터 받음.
//


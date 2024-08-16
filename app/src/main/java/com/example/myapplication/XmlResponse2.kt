package com.example.myapplication

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

// MyXmlItem2 데이터 클래스 정의
@Xml(name = "item")
data class MyXmlItem2(
    @PropertyElement
    val krnm: String?, // 국명
    @PropertyElement
    val kornFamlNm: String?, // 과명
    @PropertyElement
    val scnm: String?, // 학명
    @PropertyElement
    val chchNm: String?, // 한자명
    @PropertyElement
    val fturCn: String?, // 특징
    @PropertyElement
    val mdcnlInfoCn: String?, // 약물특징
    @PropertyElement
    val imgUrl: String? // 이미지

) {
    constructor() : this(null, null, null, null, null, null, null)
}

// XmlResponse2 데이터 클래스 정의
@Xml(name = "response")
data class XmlResponse2(
    @Element
    val body: MyXmlBody2
)

@Xml(name = "body")
data class MyXmlBody2(
    @Element
    val items: MyXmlItems2
)

@Xml(name = "items")
data class MyXmlItems2(
    @Element
    val item: MutableList<MyXmlItem2>
)

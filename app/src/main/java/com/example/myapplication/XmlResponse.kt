package com.example.myapplication

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class XmlResponse(
    @Element
    val body: myXmlBody
)

@Xml(name = "body")
data class myXmlBody(
    @Element
    val items: myXmlItems
)

@Xml(name = "items")
data class myXmlItems(
    @Element
    val item: MutableList<myXmlItem>
)

@Xml(name = "item")
data class myXmlItem(
    @PropertyElement
    val krnm: String?, // 국명
    @PropertyElement
    val kornFamlNm: String?, // 과명
    @PropertyElement
    val fturCn: String?, // 특징
    @PropertyElement
    val scnm: String?,// 학명
    @PropertyElement
    val excptnMttrCn: String?, // 지역
    @PropertyElement
    val rrtyPrntGrdCd: String?, // 희귀특성
    @PropertyElement
    val imgUrl: String? // 이미지
) {
    constructor() : this(null, null, null, null, null, null, null)
}
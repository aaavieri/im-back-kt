package com.wwx.imback.error

import java.lang.Exception

class WechatException(val errMsg: String = "", val errCode: Int = 9): Exception()
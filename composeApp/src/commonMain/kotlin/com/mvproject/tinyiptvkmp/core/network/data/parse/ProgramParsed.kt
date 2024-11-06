package com.mvproject.tinyiptvkmp.core.network.data.parse

import com.mvproject.tinyiptvkmp.utils.CommonUtils.empty

data class ProgramParsed(
    var start: String = String.empty,
    var stop: String = String.empty,
    var channel: String = String.empty,
    var title: String = String.empty,
    var desc: String? = null,
)

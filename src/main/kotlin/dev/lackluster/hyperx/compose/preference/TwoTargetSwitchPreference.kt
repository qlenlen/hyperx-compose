package dev.lackluster.hyperx.compose.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import dev.lackluster.hyperx.compose.activity.SafeSP
import dev.lackluster.hyperx.compose.base.DrawableResIcon
import dev.lackluster.hyperx.compose.base.ImageIcon
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentColors
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.SwitchColors
import top.yukonga.miuix.kmp.basic.SwitchDefaults

@Composable
fun TwoTargetSwitchPreference(
  icon: ImageIcon? = null,
  title: String,
  summary: String? = null,
  key: String? = null,
  defValue: Boolean = false,
  enabled: Boolean = true,
  titleColor: BasicComponentColors = BasicComponentDefaults.titleColor(),
  summaryColor: BasicComponentColors = BasicComponentDefaults.summaryColor(),
  switchColors: SwitchColors = SwitchDefaults.switchColors(),
  onCheckedChange: ((Boolean) -> Unit)? = null,
  onClick: ((Boolean) -> Unit)? = null, // 左侧点击
) {
  var checked by remember {
    mutableStateOf(key?.let { SafeSP.getBoolean(it, defValue) } ?: defValue)
  }
  val updatedOnCheckedChange by rememberUpdatedState(onCheckedChange)

  fun toggle() {
    checked = !checked
    key?.let { SafeSP.putAny(it, checked) }
    updatedOnCheckedChange?.invoke(checked)
  }

  BasicComponent(
    insideMargin = PaddingValues(
      (icon?.getHorizontalPadding() ?: 16.dp),
      16.dp,
      12.dp, // 右侧稍微小一点，给 two-target 留空间
      16.dp
    ),
    title = title,
    titleColor = titleColor,
    summary = summary,
    summaryColor = summaryColor,
    startAction = {
      icon?.let { DrawableResIcon(it) }
    },
    endActions = {
      TwoTargetSwitch(
        checked = checked,
        enabled = enabled,
        colors = switchColors,
        onToggle = { toggle() }
      )
    },
    onClick = {
      if (enabled) {
        onClick?.invoke(checked)
      }
    },
    enabled = enabled
  )
}


@Composable
private fun TwoTargetSwitch(
  checked: Boolean,
  enabled: Boolean,
  colors: SwitchColors,
  onToggle: () -> Unit
) {
  androidx.compose.foundation.layout.Row(
    modifier = androidx.compose.ui.Modifier
      .clickable(
        enabled = enabled,
        onClick = onToggle
      ),
    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
  ) {
    // 左侧小竖线（点击提示）
    androidx.compose.foundation.layout.Box(
      modifier = androidx.compose.ui.Modifier
        .padding(end = 12.dp)
        .width(2.dp)
        .height(24.dp)
        .background(
          color = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.4f),
          shape = androidx.compose.foundation.shape.RoundedCornerShape(1.dp)
        )
    )

    Switch(
      checked = checked,
      onCheckedChange = { onToggle() },
      enabled = enabled,
      colors = colors
    )
  }
}

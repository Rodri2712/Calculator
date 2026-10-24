package pe.edu.upc.calculator.features.calculator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.remember
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import pe.edu.upc.calculator.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.BoxWithConstraints
import pe.edu.upc.calculator.features.calculator.application.FormatExpressionUseCase
import pe.edu.upc.calculator.features.calculator.domain.HistoryItem
import pe.edu.upc.calculator.core.designsystems.theme.GreenAccent
import pe.edu.upc.calculator.core.designsystems.theme.CalculatorBackground
import pe.edu.upc.calculator.core.designsystems.theme.HistoryButtonColor
import pe.edu.upc.calculator.core.designsystems.theme.HistoryDividerColor
import pe.edu.upc.calculator.core.designsystems.theme.SecondaryTextColor

@Composable
fun CalculatorHistory(
    history: List<HistoryItem>,
    onClearHistory: () -> Unit,
    onHistoryItemClick: (HistoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatExpression = remember {
        FormatExpressionUseCase()
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .width(maxWidth * 0.75f + 12.dp)
                .fillMaxHeight()
                .background(CalculatorBackground)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(
                        start = 16.dp,
                        end = 12.dp,
                        top = 12.dp,
                        bottom = 8.dp
                    ),
            horizontalAlignment = Alignment.Start
        ) {
                val chronologicalHistory = history.asReversed()
                val listState = rememberLazyListState()

                LaunchedEffect(chronologicalHistory.size) {
                    if (chronologicalHistory.isNotEmpty()) {
                        listState.scrollToItem(chronologicalHistory.lastIndex)
                    }
                }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                ) {
                    items(chronologicalHistory) { item ->
                    Surface(
                        onClick = { onHistoryItemClick(item) },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = formatExpression(item.operation),
                                modifier = Modifier.fillMaxWidth(),
                                color = SecondaryTextColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.End
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "= ${formatExpression(item.result)}",
                                modifier = Modifier.fillMaxWidth(),
                                color = GreenAccent,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.End
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (history.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.history_empty),
                                color = SecondaryTextColor,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                onClick = onClearHistory,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = HistoryButtonColor
            ) {
                Text(
                    text = stringResource(R.string.history_clear),
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 12.dp
                    ),
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

                Spacer(modifier = Modifier.height(8.dp))
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(HistoryDividerColor)
            )
        }
    }
}
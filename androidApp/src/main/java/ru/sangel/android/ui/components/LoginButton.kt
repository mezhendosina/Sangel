package ru.sangel.android.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.sangel.android.ui.theme.SangelTheme

@Composable
fun LoginButton(
    onClick: () -> Unit,
    content: @Composable (RowScope) -> Unit,
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 78.dp, vertical = 16.dp),
        modifier = Modifier.padding(bottom = 64.dp),
        content = content,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
    )
}

@Preview
@Composable
private fun PreviewLoginButton() {
    SangelTheme {
        LoginButton(onClick = { /*TODO*/ }) {
            Text("123")
        }
    }
}
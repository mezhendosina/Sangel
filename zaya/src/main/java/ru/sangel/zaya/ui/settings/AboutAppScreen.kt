package ru.sangel.zaya.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.sangel.zaya.BuildConfig
import ru.sangel.zaya.R
import ru.sangel.zaya.ui.components.AboutAppButton
import ru.sangel.zaya.ui.components.AboutTeamItem
import ru.sangel.zaya.ui.theme.SangelTheme
import ru.sangel.zaya.presentation.components.main.settings.about.AboutAppComponent

@Composable
fun AboutAppScreen(
    component: AboutAppComponent,
    modifier: Modifier = Modifier,
) {
    val model by component.model.subscribeAsState()
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    LazyColumn(
        modifier =
        modifier
            .fillMaxSize()
            .padding(horizontal = 46.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painterResource(id = R.drawable.ic_about_app_logo), null)
                Spacer(modifier = Modifier.size(24.dp))
                Column {
                    Text(
                        stringResource(id = R.string.sangel).uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        stringResource(R.string.version, BuildConfig.VERSION_NAME),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.size(16.dp))
            Text(stringResource(id = R.string.team), style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.size(24.dp))
        }
        items(model.team) {
            AboutTeamItem(aboutTeamEntity = it)
        }
        item {
            Spacer(modifier = Modifier.size(40.dp))
            AboutAppButton(
                leadingImage = painterResource(id = R.drawable.ic_vk),
                text = stringResource(R.string.we_in_vk),
            ) {
                uriHandler.openUri(context.getString(R.string.uri_vk_group))
            }
            Spacer(modifier = Modifier.size(8.dp))
            AboutAppButton(
                leadingImage = painterResource(id = R.drawable.ic_mail),
                text = stringResource(R.string.mail_us),
            ) {
                uriHandler.openUri(context.getString(R.string.contact_us_email))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8F8F6)
@Composable
private fun PreviewAboutAppScreen() {
    SangelTheme {
        AboutAppScreen(AboutAppComponent.stubComponent())
    }
}

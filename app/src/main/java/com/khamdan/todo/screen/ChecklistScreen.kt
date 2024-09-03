package com.khamdan.todo.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.khamdan.todo.ChecklistViewModel
import com.khamdan.todo.InputData

@Composable
fun ChecklistScreen(viewModel: ChecklistViewModel) {
    val checklists by viewModel.checklists.observeAsState(emptyList())
    val inputData by viewModel.inputData.observeAsState(InputData())
    val triggerToast by viewModel.triggerToast.observeAsState(null)

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getChecklists()
    }

    LaunchedEffect(triggerToast) {
        triggerToast?.let {
            Toast.makeText(
                context,
                if (it.second.isNullOrBlank()) "Error Server" else it.second,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 60.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(checklists) { checklist ->
                Column(
                    Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .padding(10.dp),
                ) {
                    Text(text = checklist.name, fontSize = 24.sp)

                    Spacer(modifier = Modifier.padding(vertical = 2.dp))

                    checklist?.items?.forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.clickable {
                                    viewModel.actionEditChecklistItem(
                                        checklist.id,
                                        item.id,
                                        item.name
                                    )
                                },
                                text = item.name, fontSize = 20.sp
                            )
                            Checkbox(
                                checked = item.itemCompletionStatus,
                                onCheckedChange = {
                                    viewModel.editChecklistItemStatus(checklist.id, item.id)
                                })
                            Button(
                                modifier = Modifier
                                    .height(36.dp),
                                onClick = {
                                    viewModel.deleteChecklistItem(checklist.id, item.id)
                                }
                            ) {
                                Text("Delete")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = 2.dp))

                    Button(
                        modifier = Modifier
                            .height(36.dp),
                        onClick = {
                            viewModel.actionAddChecklistItem(checklist.id)
                        },
                    ) {
                        Text("Add Item")
                    }

                    Spacer(modifier = Modifier.padding(vertical = 4.dp))

                    Button(
                        modifier = Modifier
                            .height(36.dp),
                        onClick = { viewModel.deleteChecklist(checklist.id) },
                    ) {
                        Text("Delete")
                    }
                }

            }
        }

        Button(
            modifier = Modifier
                .padding(top = 10.dp)
                .height(36.dp),
            onClick = { viewModel.actionAddChecklist() }
        ) {
            Text("Add Checklist")
        }
    }

    InputDialog(
        showDialog = inputData.inputActive,
        initial = inputData.text,
        onDismiss = { viewModel.actionCancel() },
        onConfirm = { viewModel.actionSave(it) })
}

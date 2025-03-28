
package com.akilas.solarsaverprojectakilas.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(nameList: List<Name>, chosenOption: Name, onOptionChange: (Name) -> Unit, expanded: Boolean, setExpanded: (Boolean)-> Unit, label: String) {

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = setExpanded
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .padding(5.dp),
            readOnly = true,
            value = chosenOption.fetchName(),
            onValueChange = {},
            label = { Text(label) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { setExpanded(false) }
        ) {
            nameList.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.fetchName()) },
                    onClick = {
                        onOptionChange(item)
                        setExpanded(false)
                    }
                )
            }
        }
    }
}

package com.tcron.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class HelpSection(
    val id: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val items: List<HelpItem>
)

data class HelpItem(
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    onNavigateBack: () -> Unit = {}
) {
    val helpSections = remember {
        listOf(
            HelpSection(
                "getting_started",
                "Primeiros Passos",
                Icons.Default.RocketLaunch,
                listOf(
                    HelpItem(
                        "Como criar minha primeira tarefa?",
                        "Toque no botão + na tela inicial, selecione 'Criar agendamento' e configure o horário e comando desejado."
                    ),
                    HelpItem(
                        "O que é um crontab?",
                        "Crontab é um formato de agendamento que define quando uma tarefa deve ser executada usando 5 campos: minuto, hora, dia do mês, mês e dia da semana."
                    ),
                    HelpItem(
                        "Como funciona o terminal integrado?",
                        "O terminal permite executar comandos diretamente no dispositivo. Acesse através do botão terminal na tela inicial."
                    )
                )
            ),
            HelpSection(
                "tasks",
                "Gerenciamento de Tarefas",
                Icons.Default.Schedule,
                listOf(
                    HelpItem(
                        "Como editar uma tarefa existente?",
                        "Toque na tarefa na lista da tela inicial para abrir os detalhes e opções de edição."
                    ),
                    HelpItem(
                        "Por que minha tarefa não executou?",
                        "Verifique se o aplicativo tem permissões necessárias, se o dispositivo não estava em modo de economia de energia e se o comando está correto."
                    ),
                    HelpItem(
                        "Como ver o histórico de execuções?",
                        "Na tela inicial, role para baixo até o card 'Histórico de Execuções' ou acesse através do menu lateral > Logs."
                    )
                )
            ),
            HelpSection(
                "scripts",
                "Scripts e Automação",
                Icons.Default.Code,
                listOf(
                    HelpItem(
                        "Quais linguagens são suportadas?",
                        "TCron suporta scripts Shell (.sh), Python (.py) e comandos do sistema Android."
                    ),
                    HelpItem(
                        "Como carregar um script externo?",
                        "Use o botão + > 'Carregar script' para importar arquivos .sh ou .py do seu dispositivo."
                    ),
                    HelpItem(
                        "Scripts precisam de permissão root?",
                        "Não necessariamente. Muitos comandos funcionam sem root, mas alguns recursos do sistema podem precisar."
                    )
                )
            ),
            HelpSection(
                "troubleshooting",
                "Solução de Problemas",
                Icons.Default.Help,
                listOf(
                    HelpItem(
                        "O aplicativo não inicia após o boot",
                        "Verifique se a opção 'Iniciar no boot' está ativa nas Configurações e se o aplicativo tem permissão de executar em segundo plano."
                    ),
                    HelpItem(
                        "Como verificar se tenho acesso root?",
                        "Vá em Configurações > Sistema > Status Root e toque em 'Verificar' para testar o acesso root."
                    ),
                    HelpItem(
                        "Tarefas param de funcionar após um tempo",
                        "Isso pode ser devido ao modo de economia de energia. Adicione o TCron à lista de aplicativos excluídos da otimização de bateria."
                    )
                )
            )
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajuda / Tutorial") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Open documentation */ }) {
                        Icon(Icons.Default.OpenInBrowser, contentDescription = "Documentação online")
                    }
                    
                    IconButton(onClick = { /* TODO: Contact support */ }) {
                        Icon(Icons.Default.Email, contentDescription = "Contatar suporte")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(helpSections) { section ->
                HelpSectionCard(section)
            }
            
            item {
                ContactCard()
            }
        }
    }
}

@Composable
private fun HelpSectionCard(section: HelpSection) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        section.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Recolher" else "Expandir"
                    )
                }
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                
                section.items.forEach { item ->
                    HelpItemCard(item)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun HelpItemCard(item: HelpItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = item.answer,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ContactCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.ContactSupport,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Precisa de mais ajuda?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Entre em contato conosco para suporte técnico ou sugestões",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { /* TODO: Open email */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Email, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Email")
                }
                
                OutlinedButton(
                    onClick = { /* TODO: Open GitHub */ },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(Icons.Default.Code, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("GitHub")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HelpScreenPreview() {
    MaterialTheme {
        HelpScreen()
    }
}
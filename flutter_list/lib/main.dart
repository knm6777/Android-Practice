import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_list/list_page.dart';
import 'package:flutter_list/app_state.dart';



class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Navigation Test',
      initialRoute: ListPage.nav_url,
      routes: {
        ListPage.nav_url: (context) => ListPage(),
      },
    );
  }
}

void main() => runApp(ChangeNotifierProvider(
  // MyApp() 포함하여 자손 위젯들에게 AppState()를 제공
    create: (context) => AppState(),
    child: MyApp()));
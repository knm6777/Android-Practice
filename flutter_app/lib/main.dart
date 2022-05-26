import "package:flutter/material.dart";

class MyForm extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => MyState();
}
class MyState extends State<MyForm> {
  TextEditingController textEditingController = TextEditingController();
  String displayText = 'Hello, Flutter';
  @override
  Widget build(BuildContext context) {
    return Center(child: Column(mainAxisAlignment: MainAxisAlignment.center,
    children: [
      Text(displayText),
      TextField(controller: textEditingController),
      RaisedButton(child: Text("Change Test!!"),
          onPressed: () => setState(() {
            displayText = textEditingController.text;
          }
          ))
    ]));
  }
/*
  void _updateWidget() {
    Scaffold.of(context).showSnackBar(
        SnackBar(content:Text("Hi" + textEditingController.text))
    );
    setState(() {});
  }

 */
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "test",
      home: Scaffold(
        appBar: AppBar(title: Text("Title")),
        body: MyForm()
      )
    );
  }
}

void main() => runApp(MyApp());
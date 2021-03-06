/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform, StyleSheet, Text, View,
  requireNativeComponent, NativeModules,
  DeviceEventEmitter, Dimensions
} from 'react-native';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});
const { CustomToast } = NativeModules;
const CustomImageView = requireNativeComponent('CustomImageView');
const AdsView = requireNativeComponent('AdsView');
const EVENT_NAME = 'EVENT';

export default class App extends Component {
  componentWillMount() {
    DeviceEventEmitter.addListener(EVENT_NAME, function (e) {
      CustomToast.show(e.message, CustomToast.SHORT);
    });
  }

  render() {
    CustomToast.testCallback(async (message) => {
      CustomToast.show(message, CustomToast.SHORT);
      message = await CustomToast.testPromise();
      CustomToast.show(message, CustomToast.SHORT);
      CustomToast.testEvent(EVENT_NAME);
    });
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Welcome to React Native!</Text>
        <Text style={styles.instructions}>To get started, edit App.js</Text>
        <Text style={styles.instructions}>{instructions}</Text>
        <CustomImageView src={'https://3.bp.blogspot.com/-LlPibkiuaog/WZYmP8MNyXI/AAAAAAAAAFY/2f-L0UOZwjYUn6mceia1Pe5K-OY4Lp4twCLcBGAs/s320/Persebaya%2BSurabaya.jpg'}
          style={{ width: 320, height: 200 }} />
        <View>
          <AdsView style={{ width: 160, height: 50 }} />
        </View>
      </View>
    );
  }
}

const width = Dimensions.get('window').width; //full width
const height = Dimensions.get('window').height; //full height

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  }
});

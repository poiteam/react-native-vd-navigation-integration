/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';
import type {PropsWithChildren} from 'react';
import type {Node} from 'react';
import {
  StyleSheet,
  Text,
  useColorScheme,
  View,
  NativeModules,
  Button,
} from 'react-native';

import {
  Colors,
} from 'react-native/Libraries/NewAppScreen';

type SectionProps = PropsWithChildren<{
  title: string;
}>;

function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  return (
    <View style={{flex: 1}}>
    <View style={{height: 100, backgroundColor:"blue"}}/>
    <View style={{height: 100, backgroundColor:"blue"}}>
    <Button
      title="Show VdNavigation"
      onPress={() => {
        NativeModules.PoilabsNavigationBridge.showPoilabsVdNavigation();
      }
      }
    />
    </View>
</View>
  );
}

export default App;

import { View, TextInput, Button, StyleSheet } from 'react-native';
import React, { useState } from 'react';

function buscar(){

    const [query, setQuery] = useState('');

    const handleSearch = () => {
      onSearch(query);
    };
    return (
        <View style={styles.container}>
          <TextInput
            style={styles.input}
            placeholder="Buscar pokemon..."
            onChangeText={setQuery}
          />
         
        </View>
      );
}
const styles = StyleSheet.create({
    container: {
      flexDirection: 'row',
      alignItems: 'center',
      paddingHorizontal: 10,
      paddingVertical: 10,
      marginBottom: 10,
    },
    input: {
      flex: 1,
      borderWidth: 1,
      borderColor: '#060270',

      borderRadius: 5,
      paddingHorizontal: 20,
      marginRight: 20,
    },
  });
  
  export default buscar;
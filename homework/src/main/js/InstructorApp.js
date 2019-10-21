import React, { Component } from 'react';

import { BrowserRouter as Router, Route, Switch, Link } from 'react-router-dom'
import ListPersonComponent from "./component/ListPersonComponent";

class InstructorApp extends Component {
    render() {
        return (
            <Router>
                <>                    
                    <Switch>
                    	<Route path="/" exact component={ListPersonComponent} />        	
                    </Switch>
                </>
            </Router>
        )
    }
}

export default InstructorApp;
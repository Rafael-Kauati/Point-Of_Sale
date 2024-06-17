
import React, { useState, useEffect } from 'react';
import { ResponsiveContainer, PieChart, Pie, Legend, Cell,RadialBarChart, RadialBar,LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip} from 'recharts';
import '../assets/css/Admin_Graf.css';
import BaseUrl from '../components/BaseUrl';
import axios from 'axios';

const Admin_Graf = () => {
  const [chartData, setChartData] = useState([]);
  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#A569BD', '#F4D03F', '#5DADE2', '#58D68D', '#FA8072', '#C39BD3', '#F5B041', '#AED6F1' ];
  const [activeChart, setActiveChart] = useState('pie');

  useEffect(() => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('No user ID found');
      return;
    }
  
    const fetchChartData = () => {
      axios.get(`${BaseUrl()}api/sales`)
        .then(response => {
          const formattedData = Object.entries(response.data).map(([key, value]) => ({ name: `${key}`, value }));
          setChartData(formattedData);
        })
        .catch(error => {
          console.error('Error fetching chart data:', error);
        });
    };
  
    fetchChartData();
  
    const interval = setInterval(() => {
      fetchChartData();
    }, 5000);
  
    return () => clearInterval(interval);
  }, []);

  
  const calculateTotalSales = () => {
    return chartData.reduce((acc, curr) => acc + curr.value, 0);
  };


  const getMinMaxSalesEmployee = () => {
    if (chartData.length === 0) {
      return { max: null, min: null };
    }

    const sortedData = [...chartData].sort((a, b) => b.value - a.value);
    return { max: sortedData[0], min: sortedData[sortedData.length - 1] };
  };

  const { max, min } = getMinMaxSalesEmployee();
  const style = {
    top: '50%',
    right: 0,
    transform: 'translate(0, -50%)',
    lineHeight: '24px',
  };
  const renderLines = () => {
    const employeeNames = chartData.length > 0 ? Object.keys(chartData[0]).filter(key => key !== 'name') : [];

    return employeeNames.map((emp, index) => (
      <Line
        key={emp}
        type="monotone"
        dataKey={emp}
        stroke={COLORS[index % COLORS.length]}
        activeDot={{ r: 8 }}
      />
    ));
  };

  return (
    <div className="graficos-container">
      <div className="graficos-header">
        <h2 className="graficos-title">Admin graphics</h2>
      </div>
      <div className="graficos-content-container">
        
      <div className="graficos-info">
        <h1>Manager Information </h1>
        <br /><br />
        <p>
          <strong>💰 Total invoiced</strong> {calculateTotalSales()} ª
        </p>
        <p>
          <strong>👥 Number of employees: </strong>{chartData.length} º
        </p>
        <p>
          <strong>🥇 Employee with Biggest Sale:</strong> {max ?  `${max.name} (${max.value}€)` : 'N/A' }
        </p>
        <p>
          <strong>🥈 Employee with Lowest Sales:</strong> {min ? `${min.name} (${min.value}€)` : 'N/A'}
        </p>
        <br /><br />
        
        
        <div className='graficos-info-2'>
        <br />
          <h1>Graphics</h1>
        
                    <button onClick={() => setActiveChart('pie')} className="graficos-button"><span>Gráficos pizza</span></button>
                    <br />
                    <button onClick={() => setActiveChart('radial')} className="graficos-button"> <span>Gráficos circloo</span></button>
                    <br />
                    <button onClick={() => setActiveChart('line')} className="graficos-button"><span>Gráficos Linha</span></button>
                      
        </div>
       

      </div>

        <div className="graficos-content">
          <h2>Total Sales </h2>
          <div className="graficos-content-container" style={{ width: '100%', height: '500px' }}>
          {activeChart === 'pie' && (
            <ResponsiveContainer>
              <PieChart>
                <Pie
                  dataKey="value"
                  data={chartData}
                  label
                  labelLine={false}
                >
                  {
                    chartData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))
                  }
                </Pie>
                <Legend />
              </PieChart>
            </ResponsiveContainer>
              )}
            
            {activeChart === 'radial' && (
              <ResponsiveContainer width="100%" height="100%">
            <RadialBarChart cx="50%" cy="50%" innerRadius="20%" outerRadius="70%" barSize={20} data={chartData}>
              <RadialBar
                minAngle={15}
                label={{ position: 'insideStart', fill: '#fff' }}
                background
                clockWise
                dataKey="value" 
              >
                {
                  chartData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))
                }
              </RadialBar>
              <Legend iconSize={10} layout="vertical" verticalAlign="middle" wrapperStyle={style} />
            </RadialBarChart>
            </ResponsiveContainer>
             )}
            {activeChart === 'line' && (
            <ResponsiveContainer width="100%" height="100%">
              <LineChart
                width={500}
                height={300}
                data={chartData}
                margin={{
                  top: 5,
                  right: 30,
                  left: 20,
                  bottom: 5,
                }}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" /> 
                <YAxis />
                <Tooltip />
                <Legend />
                {renderLines()}
              </LineChart>
            </ResponsiveContainer>
          )}
         
             

          </div>
        </div>
      </div>
    </div>
  );
};

export default Admin_Graf;

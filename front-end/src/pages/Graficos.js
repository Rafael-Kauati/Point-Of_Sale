
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Legend, Cell } from 'recharts';
import '../assets/css/Graficos.css';
import axios from 'axios';
import BaseUrl from '../components/BaseUrl';


const Graficos = () => {
  const { userId } = useParams();
  const [chartData, setChartData] = useState([]);

  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

  useEffect(() => {
    const fetchData = async () => {
      let id = userId || localStorage.getItem('userId');
      if (!id) {
        console.error('UserID is undefined');
        return;
      }
      try {
        const response = await axios.get(`${BaseUrl()}api/sales/${id}`);
        setChartData(response.data);
      } catch (error) {
        console.error('Error fetching data: ', error);
      }
    };
    fetchData();
  }, [userId]);
  
  console.log(chartData);

  return (
    <div className="graficos-container">
      <div className="graficos-content">
        <div className="employee-header">
          <h2 className="employee-title">Employee Graphics</h2>
        </div>
        <div className="graficos-layout">
          <div className="graficos-info">
          <h1>Manager Information </h1>
            <br /><br />
            <p>
              <strong>💰 Total invoiced</strong> 
            </p>
            <p>
              <strong>👥 Number of employees: </strong>{chartData.length} º
            </p>
            <p>
              <strong>🥇 Employee with Biggest Sale:</strong> 
            </p>
            <p>
              <strong>🥈 Employee with Lowest Sales:</strong>
            </p>
            <br /><br />
          
          </div>
          <div className="graficos-chart-container">
          {/* <ResponsiveContainer width="100%" height={500 }>
            <AreaChart
              width={500}
              height={200}
              data={chartData}
              syncId="anyId"
              margin={{
                top: 10,
                right: 30,
                left: 0,
                bottom: 0,
              }}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Area type="monotone" dataKey="pv" stroke="#82ca9d" fill="#82ca9d" />
            </AreaChart>
          </ResponsiveContainer> */}
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
          </div>
        </div>
      </div>
    </div>
  );
};

export default Graficos;

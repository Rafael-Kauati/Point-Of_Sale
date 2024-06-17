
import React, { useState, useEffect } from 'react';
import { Line } from 'react-chartjs-2';
import axios from 'axios';
import BaseUrl from './BaseUrl';

const SalesChart = ({ employeeId }) => {
  const [salesData, setSalesData] = useState([]);

  useEffect(() => {
    axios.get(BaseUrl() + `/api/sales/${employeeId}`)
    
      .then(response => {
        
        const fetchedData = response.data;
        const chartData = fetchedData.reduce((acc, sale) => {
          acc.labels.push(sale.date);
          acc.data.push(sale.totalSales);
          return acc;
        }, { labels: [], data: [] });

        setSalesData(chartData);
        console.log(response.data)
      })
      .catch(error => console.error("Erro ao buscar dados", error));
  }, [employeeId]);

  const data = {
    labels: salesData.labels,
    datasets: [
      {
        label: 'Total de Vendas',
        data: salesData.data,
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1
      }
    ]
  };

  const options = {
    scales: {
      y: {
        beginAtZero: true
      }
    }
  };

  return <Line data={data} options={options} />;
};

export default SalesChart;
